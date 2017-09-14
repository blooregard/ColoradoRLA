import {
    put,
    select,
    takeLatest,
} from 'redux-saga/effects';

import notice from 'corla/notice';


function* importCvrExportOk(action: any): IterableIterator<any> {
    const { data } = action;
    const { sent } = data;

    notice.ok(`Imported CVR export "${sent.filename}".`);
}

function* importCvrExportFail(action: any): IterableIterator<any> {
    const { data } = action;
    const { received, sent } = data;

    switch (sent.hash_status) {
    case 'MISMATCH': {
        notice.danger('Failed to import CVR export.');
        notice.warning('Please verify that the hash matches the file to be uploaded.');
        break;
    }
    default: {
        notice.danger('Failed to import CVR export.');
        notice.warning('Please verify that the uploaded file is a valid CVR export.');
        break;
    }
    }
}

function* importCvrExportNetworkFail(): IterableIterator<any> {
    notice.danger('Network error: failed to upload CVR export.');
}

function* uploadCvrExportOk(action: any): IterableIterator<any> {
    const { data } = action;
    const { received } = data;

    notice.ok(`Uploaded CVR export "${received.filename}".`);
}

function* uploadCvrExportFail(action: any): IterableIterator<any> {
    notice.danger('Failed to upload CVR export.');
}

function* uploadCvrExportNetworkFail(): IterableIterator<any> {
    notice.danger('Network error: failed to upload CVR export.');
}

function createUploadingCvrExport(uploading: boolean) {
    function* uploadingCvrExport(action: any): IterableIterator<any> {
        const data = { uploading };

        yield put({ data, type: 'UPLOADING_CVR_EXPORT' });
    }

    return uploadingCvrExport;
}

const UPLOADING_FALSE = [
    'IMPORT_CVR_EXPORT_FAIL',
    'IMPORT_CVR_EXPORT_NETWORK_FAIL',
    'IMPORT_CVR_EXPORT_OK',
    'UPLOAD_CVR_EXPORT_FAIL',
    'UPLOAD_CVR_EXPORT_NETWORK_FAIL',
];

const UPLOADING_TRUE = [
    'UPLOAD_CVR_EXPORT_SEND',
];


export default function* uploadCvrExportSaga() {
    yield takeLatest('IMPORT_CVR_EXPORT_OK', importCvrExportOk);
    yield takeLatest('IMPORT_CVR_EXPORT_FAIL', importCvrExportFail);
    yield takeLatest('IMPORT_CVR_EXPORT_NETWORK_FAIL', importCvrExportNetworkFail);

    yield takeLatest('UPLOAD_CVR_EXPORT_OK', uploadCvrExportOk);
    yield takeLatest('UPLOAD_CVR_EXPORT_FAIL', uploadCvrExportFail);
    yield takeLatest('UPLOAD_CVR_EXPORT_NETWORK_FAIL', uploadCvrExportNetworkFail);

    yield takeLatest(UPLOADING_FALSE, createUploadingCvrExport(false));
    yield takeLatest(UPLOADING_TRUE, createUploadingCvrExport(true));
}
